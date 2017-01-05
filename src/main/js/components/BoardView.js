import CreateDialog from "./CreateDialog";
import ItemList from "./ItemList";
import ContentViewer from "./ContentViewer";

const React = require('react');
const client = require('../client');

const srcPath = '/src/attach/';
const root = '/rest/api';
let searchRoot = root;
let uri = '';

class BoardView extends React.Component {

    constructor(props) {
        super(props);
        searchRoot = searchRoot.concat('/threads/search/board');
        uri = '/'.concat(this.props.params.boardName);

        this.state = {
            items: [],
            attributes: [],
            pageSize: 20,
            links: {},
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
    onCreate(form) {
      const request = {
              method: 'POST',
              path: '/res/submit',
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
        let params = {
            board: true,
            items: this.state.items,
            links: this.state.links,
            pageSize: this.state.pageSize,
            onNavigate: this.onNavigate,
            onDelete: this.onDelete,
            onThumbClick: this.onThumbClick,
            updatePageSize:this.updatePageSize
        };
        return (
            <div onClick={this.onBlankClick}>
                <a href="/">Home</a>
                <CreateDialog attributes={this.state.attributes} boardName={this.props.params.boardName}
                              onCreate={this.onCreate}/>
                {this.state.items ? <ItemList params={params}/>
                        : null}
                <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
            </div>
        )
    }
}
BoardView.propTypes = {
  params: React.PropTypes.object.isRequired,
};
export default BoardView;
