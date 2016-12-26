import CreateDialog from "./CreateDialog";
import ItemList from "./ItemList";

const React = require('react');
const client = require('../client');
const follow = require('../follow');
//const Link = require('react-router').Link;

const root = '/rest/api';
let searchRoot = root;
let uri = '';

class BoardView extends React.Component {

    constructor(props) {
        super(props);
        searchRoot = searchRoot.concat('/threads/search/board');
        uri = '/'.concat(this.props.params.boardName);

        this.state = {items: [], attributes: [], pageSize: 20, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    // tag::follow-2[]
    loadFromServer(pageSize) {
        client({
            method: 'GET', path: searchRoot, params: {
                size: pageSize,
                uri: uri
            }
        }).done(threadCollection => {
            let threads = threadCollection.entity._embedded['threads'];
            this.setState({
                items: threads,
                attributes: ['title', 'text'],
                pageSize: pageSize,
                links: threadCollection.entity._links
            });
        });
    }

    // end::follow-2[]

    // tag::create[]
    onCreate(newThread) {
        follow(client, root, ['threads']).then(threadCollection => {
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

    // tag::navigate[]
    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(threadCollection => {
            this.setState({
                items: threadCollection.entity._embedded['threads'],
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
                <CreateDialog attributes={this.state.attributes} boardName={this.props.params.boardName}
                              onCreate={this.onCreate}/>
                <ItemList items={this.state.items}
                          links={this.state.links}
                          pageSize={this.state.pageSize}
                          onNavigate={this.onNavigate}
                          onDelete={this.onDelete}
                          updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}
export default BoardView;
