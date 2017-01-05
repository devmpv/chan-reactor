// @flow

'use strict';

import MessageHeader from "./MessageHeader";

const React = require('react');
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

      this.handleThumbClick = (event: SyntheticInputEvent) => {
          this.props.onThumbClick(event.target.id);
      }
    }

    render() {
        let message = this.props.message;
        let attachThumbs = message.attachments.map(attach =>
            <span key={attach.name}>
                <img id = {attach.name} src={thumbPath+attach.name} onClick={this.handleThumbClick} />
            </span>
        );
        return (
            <div className="message">
                <MessageHeader message={message} board={this.props.board} onDelete={this.props.onDelete}/>
                {attachThumbs}
                <blockquote className="message-text">{message.text}</blockquote>
            </div>
        )
    }
}

export default Message;
