'use strict';

import MessageHeader from './MessageHeader';

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::thread[]
class Thread extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.thread);
    }

    render() {
        let thread = this.props.thread;
        return (
            <div className="threadPreview">
                <table>
                    <MessageHeader thread={thread} onDelete={this.props.onDelete}/>
                    <tr>
                        <td><blockquote>{thread.text}</blockquote></td>
                    </tr>
                </table>
            </div>
        )
    }
}
// end::thread[]

export default Thread;
