'use strict';

import MessageHeader from "./MessageHeader";

// tag::vars[]
const React = require('react');
const srcPath = '/src/attach/';
const thumbPath = srcPath+'thumbs/';
// end::vars[]

// tag::message[]
class Message extends React.Component {

    constructor(props) {
        super(props);
        this.handleThumbClick = this.handleThumbClick.bind(this);
    }

    handleThumbClick(event){
        this.props.onThumbClick(event.target.id);
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
                <MessageHeader message={message} threadView={this.props.threadView} onDelete={this.props.onDelete}/>
                {attachThumbs}
                <blockquote className="message-text">{message.text}</blockquote>
            </div>
        )
    }
}
// end::message[]

export default Message;
