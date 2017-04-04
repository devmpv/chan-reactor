'use strict'

const React = require('react');
const client = require('../client');
const stompClient = require('../websocket-listener');

import {Popover, Breadcrumb, Button, ButtonToolbar, Badge} from 'react-bootstrap';
import CreateDialog from "./CreateDialog";
import ContentViewer from "./ContentViewer";
import {СThread, CTrigger, СMessage, CThumbs} from "./Components";
import Parser from 'html-react-parser';

const root = '/rest/api';
const srcPath = '/src/attach/';
const submitPath = root + '/res/submit';
let searchRoot = root + '/messages/search/thread';

class ThreadView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            thread: {},
            createDialog: false,
            replies: {},
            pageSize: 500,
            newCount: 0,
            content: {
                src: "/img/redo.png",
                visible: false
            }
        };
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onThumbClick = this.onThumbClick.bind(this);
        this.newMessage = this.newMessage.bind(this);
        this.loadNew = this.loadNew.bind(this);
        this.onOpen = () => this.setState({ createDialog: true });
        this.onClose = () => this.setState({ createDialog: false });
        this.renderPopover = this.renderPopover.bind(this);
    }

    loadFromServer(pageSize) {
      let thread;
      client({
          method: 'GET', path: root + '/threads/' + this.props.params.threadId, params: {
              projection: 'inlineAttachments'
          }
      }).done(response => {
          thread = {
            id: response.entity.id,
            attachments: response.entity.attachments,
            title: response.entity.title,
            text: response.entity.text,
            timestamp: response.entity.timestamp,
            updated: response.entity.updated,
            board: this.props.params.boardName,
            messages: {}
          };
          thread = this.createThumbs(thread);
          thread.text = Parser(thread.text);
          client({
              method: 'GET', path: searchRoot, params: {
                  size: pageSize,
                  id: this.props.params.threadId
              }
          }).done(reply => {
              let messages = reply.entity._embedded['messages'] ? reply.entity._embedded['messages'] : [];
              for (let message of messages.reverse()) {
                  thread.messages[message.id.toString()] = message;
              }
              for (let key in thread.messages) {
                  thread.messages[key] = this.parseText(this.createThumbs(thread.messages[key]), thread, 0);
              }
              this.setState({
                  thread: thread,
                  pageSize: pageSize,
                  newCount: 0
              });
          });
      });
    }

    renderPopover(index, messageId) {
      messageId = messageId.toString();
      let thread = this.state.thread;
      let message = thread.id == messageId ? thread : thread.messages[messageId];
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
              if (thread.messages[domNode.attribs.key] || thread.id == domNode.attribs.key) {
                let id_string = domNode.attribs.key.toString();
                let list = replies[id_string] ? replies[id_string] : {};
                list[message.id.toString()] = <CTrigger key={message.id} threadId={index} messageId={message.id} render={renderPopover}/>;
                replies[id_string] = list;
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

    loadNew() {
        if (this.state.newCount == 0) {
            return;
        }
        if (!this.state.items) {
            this.loadFromServer(this.state.pageSize);
            return;
        }
        if (this.state.items.length<=20) {
            this.loadFromServer(this.state.pageSize);
            return;
        }
        client({
            method: 'GET', path: searchRoot, params: {
                size: this.state.items.length,
                page: 1,
                id: this.props.params.threadId
            }
        }).done(messages => {
            let msgList = messages.entity._embedded['messages'];
            if (!msgList) {return;}
            let items = this.state.items;
            items = items.concat(msgList);
            this.setState({
                items: items,
                newCount: this.state.newCount-msgList.length,
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

    newMessage(message) {
        this.setState({
            newCount: this.state.newCount+1
        });
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        let headers = {selector: "headers['nativeHeaders']['thread'][0] == '" + this.props.params.threadId+"'"};
      	stompClient.register([
      		{route: '/topic/newMessage', headers: headers, callback: this.newMessage}
      	]);
    }

    render() {
      let params = this.props.params;
      let thread = this.state.thread;
      let threadView = thread.id ? <СThread key={thread.id} thread={thread} replies={this.state.replies}/> : null;
      return (
          <div className="chan-style">
            <Breadcrumb>
              <Breadcrumb.Item href="/">Home</Breadcrumb.Item>
              <Breadcrumb.Item href={"/"+params.boardName}>{params.boardName}</Breadcrumb.Item>
              <Breadcrumb.Item active href={"/"+params.boardName+"/thread/"+params.threadId}>{'#'+params.threadId}</Breadcrumb.Item>
              <Breadcrumb.Item active={false}><Button onClick={this.onOpen} bsStyle="success" bsSize="xsmall">Reply</Button></Breadcrumb.Item>
            </Breadcrumb>
            {threadView}
            <CreateDialog visible={this.state.createDialog}
                          onClose={this.onClose}
                          threadId={params.threadId}
                          onCreate={this.onCreate}/>
            <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
            <div className="newCount">
              <Button bsStyle="primary" bsSize="xsmall" onClick={this.loadNew}>Refresh <Badge title="New replies">{this.state.newCount}</Badge></Button>
            </div>
          </div>
      )
    }
}
ThreadView.propTypes = {
  params: React.PropTypes.object.isRequired,
};
export default ThreadView;
