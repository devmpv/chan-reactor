'use strict';

// tag::vars[]
const React = require('react');
const Dropzone = require('react-dropzone');
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
        let title = this.refs['title'].value.trim();
        let text = this.refs['text'].value.trim();
        if (this.props.threadId) {
            form.append('thread', this.props.threadId);
        }else {
            if (title === '') {
              window.alert('Title cannot be empty!');
              return;
            }
            if (this.state.files.length === 0) {
              window.alert('Thread needs an image!');
              return;
            }
            form.append('board', this.props.boardName);
        }
        if (text === '') {
            window.alert('Text cannot be empty!');
            return;
        }
        if (this.state.files.length > 4) {
            window.alert('4 files maximum');
            return;
        }
        this.state.files.map(file => form.append(file.name, file));

        form.append('title', title);
        form.append('text', text);
        this.props.onCreate(form);

        this.refs['title'].value = '';
        this.refs['text'].value = '';
        this.setState({
            files: []
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    onDrop(acceptedFiles, rejectedFiles) {
        if (rejectedFiles.length > 0) {
            console.log(rejectedFiles);
        }
        this.setState({
            files: acceptedFiles
        });
    }

    render() {
        return (
            <div>
                <span><a href="#message">Create</a></span>
                <div id="message" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">x</a>
                        <h3>Create new</h3>
                        <form>
                            <input type="text" placeholder="title"
                                   ref="title" className="comment-title"/>
                            <div>
                                <textarea name="text" rows="6" placeholder="Message text"
                                          ref="text"
                                          className="comment-text">
                                </textarea>
                            </div>
                            <div>
                                <Dropzone onDrop={this.onDrop} maxSize={1048576} accept="image/*">
                                    <div>Try dropping some files here, or click to select files to
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
CreateDialog.propTypes = {
  threadId: React.PropTypes.string,
  boardName: React.PropTypes.string,
  onCreate: React.PropTypes.func.isRequired
}
export default CreateDialog;
