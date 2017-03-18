'use strict';

const React = require('react');
const client = require('../client');

import {Popover, Breadcrumb, Button} from 'react-bootstrap';
import CreateDialog from "./CreateDialog";
import ContentViewer from "./ContentViewer";
import {СThread, CTrigger, СMessage, CThumbs} from "./Components";
import Parser from 'html-react-parser';

const srcPath = '/src/attach/';
const thumbPath = srcPath+'thumbs/';
const root = '/rest/api';
const submitPath = root + '/res/submit';
const searchRoot = root+'/threads/search/board';
const previewPath = root + '/messages/search/preview';
const countPath = root + '/messages/search/count';
const openUrl = window.location.pathname.concat("/thread/");

class BoardView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        items: [],
        replies: {},
        pageSize: 20,
        createDialog: false,
        content: {
            src: "/img/redo.png",
            visible: false
        }
    }
    this.updatePageSize = this.updatePageSize.bind(this);
    this.onCreate = this.onCreate.bind(this);
    this.onDelete = this.onDelete.bind(this);
    this.onThumbClick = this.onThumbClick.bind(this);
    this.onNavigate = this.onNavigate.bind(this);
    this.onOpen = () => this.setState({ createDialog: true });
    this.onClose = () => this.setState({ createDialog: false });
    this.renderPopover = this.renderPopover.bind(this);
  }

  loadFromServer(pageSize) {
    this.setState({
        items: []
    });
    client({
        method: 'GET', path: searchRoot, params: {
            size: pageSize,
            uri: '/'+this.props.params.boardName
        }
    }).done(threadCollection => {
        let threads = threadCollection.entity._embedded['threads'];
        if (!threads) return;
        for (var index in threads) {
          this.getThreadDetails(threads[index], index);
        }
    });
  }

  renderPopover(index, messageId) {
    messageId = messageId.toString();
    let thread = this.state.items[index];
    let message = thread.id.toString() == messageId ? thread : thread.messages[messageId];
    return(
        <Popover bsClass="popover-custom" id={messageId}>
          <СMessage message={message} controls={<div/>} style="message" replies={this.state.replies[messageId]}/>
        </Popover>
    )
  }

  createThumbs(message) {
    message.thumbs = <CThumbs attachments={message.attachments} onThumbClick={this.onThumbClick}/>;
    return message;
  }

  parseText(message, thread, index) {
    let replies = this.state.replies;
    let renderPopover = this.renderPopover;
    message.text = Parser(message.text,{
      replace: function(domNode)  {
          if (domNode.attribs && domNode.attribs.id === 'reply-link') {
            if (thread.messages[domNode.attribs.key] || thread.id.toString() == domNode.attribs.key) {
              let list = replies[domNode.attribs.key] ? replies[domNode.attribs.key] : {};
              list[message.id.toString()] = <CTrigger key={message.id} threadId={index} messageId={message.id} render={renderPopover}/>;
              replies[domNode.attribs.key] = list;
              return <CTrigger threadId={index} messageId={domNode.attribs.key} render={renderPopover}/>
            } else {
              return <span>{'>>'+domNode.attribs.key}</span>
            }
          }
      }
    });
    this.setState({
        replies: replies
    });
    return message;
  }

  getThreadDetails(thread, index) {
      thread = this.createThumbs(thread);
      thread.text = Parser(thread.text);
      thread.messages = {};
      thread.count = 0;
      thread.board = this.props.params.boardName;
      client({
          method: 'GET', path: previewPath, params: {
              id: thread.id
          }
      }).done(preview => {
          for (let message of preview.entity._embedded.messages.reverse()) {
              thread.messages[message.id.toString()] = message;
          }
          for (let key in thread.messages) {
              thread.messages[key] = this.parseText(this.createThumbs(thread.messages[key]), thread, index);
          }
          client({
              method: 'GET', path: countPath, params: {
                  id: thread.id
              }
          }).done(count => {
              let threads = this.state.items;
              thread.count = count.entity;
              threads[index] = thread;
              this.setState({
                  items: threads
              });
          });
      });
  }

  onCreate(form) {
    const request = {
            method: 'POST',
            path: submitPath,
            entity: form,
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        };
    client(request).done(
      () => {
          this.loadFromServer(this.state.pageSize);
      });
  }

  onDelete(thread) {
      client({method: 'DELETE', path: thread._links.self.href}).done(() => {
          this.loadFromServer(this.state.pageSize);
      });
  }

  onThumbClick(event) {
    let attachName = event.target.id;
    let visible = this.state.content.visible;
    let src = srcPath + attachName;
    if (this.state.content.visible) {
      if (this.state.content.src === src || attachName === '') {
          visible = false;
          src = "/img/redo.png";
      }
    }else {
      visible = true;
    }
    this.setState({
        content: {
            src: src,
            visible: visible
        }
    });
  }

  onNavigate(navUri) {
    client({method: 'GET', path: navUri}).done(threadCollection => {
        this.setState({
            items: threadCollection.entity._embedded['threads'],
        });
    });
  }

  updatePageSize(pageSize) {
    if (pageSize !== this.state.pageSize) {
        this.loadFromServer(pageSize);
    }
  }

  componentDidMount() {
      this.loadFromServer(this.state.pageSize);
  }

  render() {
    let threadPrev = this.state.items.map(thread =>
      <СThread key={thread.id} thread={thread} replies={this.state.replies}/>
    );
    return (
      <div className="chan-style">
        <Breadcrumb>
          <Breadcrumb.Item href="/">Home</Breadcrumb.Item>
          <Breadcrumb.Item active href={"/"+this.props.params.boardName}>{this.props.params.boardName}</Breadcrumb.Item>
          <Breadcrumb.Item active={false}><Button onClick={this.onOpen} bsStyle="success" bsSize="xsmall">Create</Button></Breadcrumb.Item>
        </Breadcrumb>
        {threadPrev}
        <CreateDialog visible={this.state.createDialog}
                      onClose={this.onClose}
                      boardName={this.props.params.boardName}
                      onCreate={this.onCreate}/>
        <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
      </div>
    )
  }
}
BoardView.propTypes = {
    params: React.PropTypes.object.isRequired,
};
export default BoardView;
