'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const Dropzone = require('react-dropzone');
const client = require('../client');
// end::vars[]

// tag::create-dialog[]
class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {files: []};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.onDrop = this.onDrop.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        let form = new FormData();
        this.state.files.map(file => form.append(file.name, file));
        
        /*let newThread = {};
        newThread['title'] = ReactDOM.findDOMNode(this.refs['title']).value.trim();
        newThread['text'] = ReactDOM.findDOMNode(this.refs['text']).value.trim();
        newThread['board'] = 'boards/'.concat(this.props.boardName);*/
        
        form.append('title', ReactDOM.findDOMNode(this.refs['title']).value.trim());
        form.append('text', ReactDOM.findDOMNode(this.refs['text']).value.trim());
        form.append('board', this.props.boardName);
        
        const request = {
                method: 'POST',
                path: '/res/submit',
                entity: form,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };
        client(request).done(response => {
                console.log(response);
                ReactDOM.findDOMNode(this.refs['title']).value = '';
                ReactDOM.findDOMNode(this.refs['text']).value = '';
                this.setState({
                    files: []
                });
        });
        //this.props.onCreate(newThread);
        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    onDrop(acceptedFiles, rejectedFiles) {
        console.log('Accepted files: ', acceptedFiles);
        console.log('Rejected files: ', rejectedFiles);
        this.setState({
            files: acceptedFiles
        });
    }

    render() {
        return (
            <div>
                <a href="#createThread">Create</a>
                <div id="createThread" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create new thread</h2>
                        <form>
                            <input type="text" placeholder="title"
                                   ref="title" className="field"/>
                            <div className="message-sticker-preview">
                                <textarea name="text" rows="6" wrap="soft" placeholder="Message text"
                                          ref="text"
                                          className="qcomment">
                                </textarea>
                            </div>
                            <div width="400px">
                                <Dropzone onDrop={this.onDrop}>
                                    <div height="50px">Try dropping some files here, or click to select files to
                                        upload.
                                    </div>
                                </Dropzone>
                                {this.state.files.length > 0 ? <div>
                                    <h2>Uploading {this.state.files.length} files...</h2>
                                    <div>
                                        {this.state.files.map((file) =>
                                            <img key={file.name}
                                                 src={file.preview}
                                                 width="100px"
                                                 height="100px"
                                            />)
                                        }
                                    </div>
                                </div> : null}
                            </div>
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}
// end::create-dialog[]

export default CreateDialog;