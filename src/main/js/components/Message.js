'use strict';

import MessageHeader from "./MessageHeader";

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::message[]
class Thread extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let message = this.props.message;
        return (
            <div className="message">
                <MessageHeader message={message}
                               onDelete={this.props.onDelete}/>
                <blockquote className="message-text">{message.text}</blockquote>
            </div>
        )
    }
}
// end::message[]

export default Thread;
