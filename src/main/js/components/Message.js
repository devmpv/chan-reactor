'use strict';

import MessageHeader from './MessageHeader';

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::thread[]
class Thread extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let thread = this.props.thread;
        return (
            <div className="message">
                <MessageHeader thread={thread}
                           onDelete={this.props.onDelete}/>
                <blockquote className="message-text">{thread.text}</blockquote>
            </div>
        )
    }
}
// end::thread[]

export default Thread;
