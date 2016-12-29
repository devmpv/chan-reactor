import CreateDialog from "./CreateDialog";
import ItemList from "./ItemList";
import Message from "./Message";
import ContentViewer from "./ContentViewer";

const React = require('react');
const client = require('../client');
const follow = require('../follow');

const root = '/rest/api';
const srcPath = '/src/attach/';
let searchRoot = root;
let uri = '';

class ThreadView extends React.Component {

    constructor(props) {
        super(props);
        searchRoot = searchRoot.concat('/messages/search/thread');
        uri = 'threads/'.concat(this.props.params.threadId);
        this.state = {
            empty: true,
            thread: {},
            items: [],
            attributes: [],
            pageSize: 500,
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

    // tag::follow-2[]
    loadFromServer(pageSize) {
        this.getOPMessage();
        client({
            method: 'GET', path: searchRoot, params: {
                size: pageSize,
                uri: uri
            }
        }).done(threadCollection => {
            this.setState({
                empty: false,
                items: threadCollection.entity._embedded['messages'],
                attributes: ['title', 'text'],
                pageSize: pageSize,
                links: threadCollection.entity._links
            });
        });
    }

    // end::follow-2[]

    // tag::create[]
    onCreate(newThread) {
        follow(client, root, ['messages']).then(threadCollection => {
            return client({
                method: 'POST',
                path: threadCollection.entity._links.self.href,
                entity: newThread,
                headers: {'Content-Type': 'application/json'}
            })
        }).done(() => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    // end::create[]

    // tag::delete[]
    onDelete(thread) {
        client({method: 'DELETE', path: thread._links.self.href}).done(() => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    // end::delete[]

    // tag::thumbClick[]
    onThumbClick(attachName) {
        if (this.state.content.visible) {
            this.setState({
                content: {
                    src: "/img/redo.png",
                    visible: false
                }
            });
        } else {
            this.setState({
                content: {
                    src: srcPath + attachName,
                    visible: true
                }
            });
        }
    }

    // tag::thumbClick[]

    // tag::navigate[]
    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(threadCollection => {
            this.setState({
                items: threadCollection.entity._embedded['messages'],
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: threadCollection.entity._links
            });
        });
    }

    // end::navigate[]

    // tag::update-page-size[]
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }

    // end::update-page-size[]

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    // end::follow-1[]

    render() {
        return (
            <div>
                <a href="/">Home</a>
                <CreateDialog attributes={this.state.attributes} threadId={this.props.params.threadId}
                              onCreate={this.onCreate}/>
                {this.state.thread.attachments ? <Message message={this.state.thread} onThumbClick={this.onThumbClick}/> : <p/>}
                {this.state.items && this.state.items.length > 0 ?
                    <ItemList board="false"
                              items={this.state.items}
                              links={this.state.links}
                              pageSize={this.state.pageSize}
                              onNavigate={this.onNavigate}
                              onDelete={this.onDelete}
                              onThumbClick={this.onThumbClick}
                              updatePageSize={this.updatePageSize}/>
                : <p/>}
                <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
            </div>
        )
    }
}
export default ThreadView;