'use strict';

const React = require('react');
const Dropzone = require('react-dropzone');
const Button = require('react-bootstrap/lib/Button')
const FormGroup = require('react-bootstrap/lib/FormGroup')
const ControlLabel = require('react-bootstrap/lib/ControlLabel')
const FormControl = require('react-bootstrap/lib/FormControl')
const HelpBlock = require('react-bootstrap/lib/HelpBlock')
const Modal = require('react-bootstrap/lib/Modal')

class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {files: [], title: "", text: ""};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleTitleChange = this.handleTitleChange.bind(this);
        this.handleTextChange = this.handleTextChange.bind(this);
        this.onDrop = this.onDrop.bind(this);
    }

    getValidationState() {
        const length = this.state.title.length;
        if (this.state.text.length == 0) return 'error';
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
        let text = this.state.text.substring(0,14999);
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
            text: "",
            title: ""
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
          <Modal show={this.props.visible} onHide={this.props.onClose} backdrop={false} bsSize="small" enforceFocus={false}>
              <Modal.Body>
                  <form>
                      <FormGroup controlId="formBasicText" validationState={this.getValidationState()}>
                          <FormControl
                            type="text"
                            value={this.state.title}
                            placeholder="Enter title"
                            onChange={this.handleTitleChange}/>
                          <FormControl.Feedback />
                          <FormControl
                            componentClass="textarea"
                            value={this.state.text}
                            placeholder="Enter text"
                            onChange={this.handleTextChange}/>
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
