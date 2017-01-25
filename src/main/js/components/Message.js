'use strict';

import MessageHeader from "./MessageHeader";
import ReactAutolinker from  "./ReactAutolinker";

const React = require('react');
const Button = require('react-bootstrap/lib/Button');
const Collapse = require('react-bootstrap/lib/Collapse');
const srcPath = '/src/attach/';
const thumbPath = srcPath+'thumbs/';

export type Msg = {type: 'msg', attachments: Attachment[], text: string}
export type Attachment = {type: 'attachment', name: string}

type Props = {
  onThumbClick: () => void,
  onDelete: () => void,
  message: Msg,
  board: boolean
}

class Message extends React.Component<void, Props, void> {
    handleThumbClick: (event: SyntheticInputEvent)=>void;
    constructor(props: Props) {
      super(props);
      this.state = {open: false};
      this.handleThumbClick = (event: SyntheticInputEvent) => {
          this.props.onThumbClick(event.target.id);
      }
    }

    render() {
        let message = this.props.message;
        let attachThumbs = message.attachments.map(attach =>
            <span key={attach.name} id={attach.name} onClick={this.handleThumbClick}>
                <img className="thumb" id={attach.name} src={thumbPath+attach.name} onClick={this.handleThumbClick}/>
            </span>
        );
        let style = message.attachments.length > 1 ? "" : "multi-attach";
        return (
            <div className="post-wrapper">
                <div className="message">
                    <MessageHeader message={message} board={this.props.board} onDelete={this.props.onDelete} onDialogOpen={this.props.onDialogOpen}/>
                    <div className={style}>
                      <div>{attachThumbs}</div>
                      {message.text.length > 400 ? <div>
                        <Button bsStyle="link" onClick={ ()=> this.setState({ open: !this.state.open })}>{this.state.open ? "Hide" : "Comment is too long. Show..."}</Button>
                        <Collapse in={this.state.open}><p className="message-text">{message.text}</p></Collapse>
                      </div> :
                        <div className="message-text"><ReactAutolinker text={message.text}/></div>
                      }
                    </div>
                </div>
            </div>
        )
    }
}

export default Message;
