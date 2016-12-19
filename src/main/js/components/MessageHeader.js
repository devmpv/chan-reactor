'use strict';

// tag::vars[]
const React = require('react');
const path = window.location.pathname.concat("/thread/");
// end::vars[]


// tag::thread[]
class MessageHeader extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.thread);
    }

    render() {
        let date = (new Date(this.props.thread.timestamp)).toLocaleString();
        let replyHref = path.concat(this.props.thread.id);
        return (
            <div className="message-header">
                <span><input type="checkbox" name="delete"/></span>
                <span className="message-title">{this.props.thread.title}</span>
                <span>Anonymous</span>
                <span>{date}</span>
                <span>Id: {this.props.thread.id}</span>
                <span>
					<button onClick={this.handleDelete}>Delete</button>
				</span>
                <span>
					<button>Hide</button>
				</span>
                <span>
					<a href={replyHref}>Reply</a>
				</span>
            </div>
        )
    }
}
// end::thread[]

export default MessageHeader;
