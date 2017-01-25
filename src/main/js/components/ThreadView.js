'use strict'

import CreateDialog from "./CreateDialog";
import ItemList from "./ItemList";
import Message from "./Message";
import ContentViewer from "./ContentViewer";

const React = require('react');
const client = require('../client');
const stompClient = require('../websocket-listener');
const Button = require('react-bootstrap/lib/Button');
const ButtonToolbar = require('react-bootstrap/lib/ButtonToolbar');
const Badge = require('react-bootstrap/lib/Badge');

const root = '/rest/api';
const srcPath = '/src/attach/';
const submitPath = root + '/res/submit';
let searchRoot = root + '/messages/search/thread';

class ThreadView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            createDialog: false,
            empty: true,
            thread: {},
            items: [],
            attributes: [],
            pageSize: 500,
            newCount: 0,
            links: {},
            content: {
                src: "/img/redo.png",
                visible: false
            }
        };
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onThumbClick = this.onThumbClick.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.newMessage = this.newMessage.bind(this);
        this.loadNew = this.loadNew.bind(this);
        this.onOpen = this.onOpen.bind(this);
        this.onClose = this.onClose.bind(this);
    }

    onClose() {
        this.setState({ createDialog: false });
    }

    onOpen() {
        this.setState({ createDialog: true });
    }

    getOPMessage() {
        client({
            method: 'GET', path: root + '/threads/' + this.props.params.threadId, params: {
                projection: 'inlineAttachments'
            }
        }).done(response => {
            this.setState({
                thread: response.entity
            });
        });
    }

    loadFromServer(pageSize) {
        this.getOPMessage();
        client({
            method: 'GET', path: searchRoot, params: {
                size: pageSize,
                id: this.props.params.threadId
            }
        }).done(messages => {
            this.setState({
                empty: false,
                items: messages.entity._embedded['messages'],
                attributes: ['title', 'text'],
                pageSize: pageSize,
                newCount: 0,
                links: messages.entity._links
            });
        });
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

    onThumbClick(attachName) {
      let visible = this.state.content.visible;
      let src = srcPath+attachName;
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
                items: threadCollection.entity._embedded['messages'],
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                newCount: 0,
                links: threadCollection.entity._links
            });
        });
    }

    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
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
        let params = {board: false,
                items: this.state.items,
                links: this.state.links,
                pageSize: this.state.pageSize,
                onDialogOpen: this.onOpen,
                onNavigate: this.onNavigate,
                onDelete: this.onDelete,
                onThumbClick: this.onThumbClick,
                updatePageSize: this.updatePageSize};
        let boardLink = '/'+this.props.params.boardName;
        return (
            <div>
                <span><a href="/">Home</a></span>
                <span><a href={boardLink}>{boardLink}</a></span>
                <CreateDialog visible={this.state.createDialog}
                              onClose={this.onClose}
                              threadId={this.props.params.threadId}
                              onCreate={this.onCreate}/>
                <p/><p/>
                {this.state.thread.attachments ? <Message onDialogOpen={this.onOpen} message={this.state.thread} board={false} onThumbClick={this.onThumbClick}/> : <p/>}
                {this.state.items && this.state.items.length > 0 ?
                    <ItemList params={params}/>
                : <p/>}
                <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
                <div className="newCount">
                  <ButtonToolbar>
                      <Button onClick={this.onOpen} bsStyle="success" bsSize="xsmall">Reply</Button>
                      <Button bsStyle="primary" bsSize="xsmall" onClick={this.loadNew}>Refresh <Badge title="Omitted replies">{this.state.newCount}</Badge></Button>
                  </ButtonToolbar>
                </div>
            </div>
        )
    }
}
ThreadView.propTypes = {
  params: React.PropTypes.object.isRequired,
};
export default ThreadView;
