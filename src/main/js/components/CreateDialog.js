'use strict';

const React = require('react');
const Dropzone = require('react-dropzone');
import {Button, FormControl, FormGroup, ControlLabel, HelpBlock, Modal} from 'react-bootstrap';
import { Editor } from 'react-draft-wysiwyg';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import toolbarConfig from './ToolbarConfig';
import draftToHtml from 'draftjs-to-html';
import {convertToRaw, EditorState} from 'draft-js';

class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {files: [], title: '', editorState: EditorState.createEmpty()};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleTitleChange = this.handleTitleChange.bind(this);
        this.handleTextChange = this.handleTextChange.bind(this);
        this.onDrop = this.onDrop.bind(this);
        this.onEditorStateChange = (editorState) => this.setState({editorState});
    }

    getValidationState() {
        const length = this.state.title.length;
        if (this.state.editorState.length == 0) return 'error';
        if (length > 1) return 'success';
        else if (length > 50) return 'warning';
    }

    handleTitleChange(e) {
        this.setState({ title: e.target.value });
    }

    handleTextChange(e) {
        this.setState({ text: e.target.value });
    }

    handleSubmit(e) {
        e.preventDefault();
        let form = new FormData();
        let title = this.state.title.substring(0,49);
        let text = draftToHtml(convertToRaw(this.state.editorState.getCurrentContent()));
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

        this.setState({
            files: [],
            editorState: EditorState.createEmpty(),
            title: ''
        });
        this.props.onClose();
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
          <Modal className="create-dialog" show={this.props.visible} onHide={this.props.onClose} backdrop={false} enforceFocus={false}>
              <Modal.Body>
                  <form>
                      <FormGroup controlId="formBasicText" validationState={this.getValidationState()}>
                          <FormControl
                            type="text"
                            value={this.state.title}
                            placeholder="Enter title"
                            onChange={this.handleTitleChange}/>
                          <FormControl.Feedback />
                          <div className="editor-wrapper">
                            <Editor
                              editorState={this.state.editorState}
                              onEditorStateChange={this.onEditorStateChange}
                              toolbar={toolbarConfig}/>
                          </div>
                      </FormGroup>
                      <div>
                          <Dropzone onDrop={this.onDrop} maxSize={1048576} accept="image/*">
                              <div>Try dropping some files here, or click to select files to
                                  upload.
                              </div>
                          </Dropzone>
                          {this.state.files.length > 0 ? <div>
                              Uploading {this.state.files.length} file(s)...
                              <div>
                                  {this.state.files.map((file) =>
                                      <img key={file.name}
                                           src={file.preview}
                                           width="100px"
                                           height="100px"
                                      />)
                                  }
                              </div>
                          </div> : <span/>}
                      </div>
                  </form>
              </Modal.Body>
              <Modal.Footer>
                  <Button bsStyle="success" bsSize="xsmall" onClick={this.handleSubmit}>Create</Button>
                  <Button bsSize="xsmall" onClick={this.props.onClose}>Close</Button>
              </Modal.Footer>
          </Modal>
        )
    }
}
CreateDialog.propTypes = {
  threadId: React.PropTypes.string,
  boardName: React.PropTypes.string,
  onCreate: React.PropTypes.func.isRequired
}
export default CreateDialog;
