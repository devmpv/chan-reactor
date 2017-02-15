'use strict';

import CreateDialog from "./CreateDialog";
import ItemList from "./ItemList";
import ContentViewer from "./ContentViewer";

const React = require('react');
const client = require('../client');
const Button = require('react-bootstrap/lib/Button')
const srcPath = '/src/attach/';
const root = '/rest/api';
const submitPath = root + '/res/submit';
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
        this.onOpen = this.onOpen.bind(this);
        this.onClose = this.onClose.bind(this);
    }

    onClose() {
        this.setState({ createDialog: false });
    }

    onOpen() {
        this.setState({ createDialog: true });
    }

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
                items: threadCollection.entity._embedded['threads'],
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: threadCollection.entity._links
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
        let params = {
            board: true,
            items: this.state.items,
            links: this.state.links,
            onDialogOpen: this.onOpen,
            pageSize: this.state.pageSize,
            onNavigate: this.onNavigate,
            onDelete: this.onDelete,
            onThumbClick: this.onThumbClick,
            updatePageSize:this.updatePageSize
        };
        return (
            <div>
                <span><a href="/">Home</a></span>
                <div className="href title"><a href={"/"+this.props.params.boardName}>{"/"+this.props.params.boardName}</a></div>
                <div className="href"><Button onClick={this.onOpen} bsStyle="success" bsSize="xsmall">Create</Button></div>
                <CreateDialog visible={this.state.createDialog}
                              onClose={this.onClose}
                              boardName={this.props.params.boardName}
                              onCreate={this.onCreate}/>
                <p/>
                {this.state.items ? <ItemList params={params}/> : null}
                <ContentViewer content={this.state.content} onThumbClick={this.onThumbClick}/>
            </div>
        )
    }
}
BoardView.propTypes = {
    params: React.PropTypes.object.isRequired,
};
export default BoardView;
