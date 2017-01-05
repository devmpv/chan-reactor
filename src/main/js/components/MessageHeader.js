'use strict';

// tag::vars[]
const React = require('react');
const path = window.location.pathname.concat("/thread/");
// end::vars[]


// tag::message[]
class MessageHeader extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.message);
    }
/*<button onClick={this.handleDelete}>Delete</button>*/
    render() {
        let date = (new Date(this.props.message.timestamp)).toLocaleString();
        let replyHref = this.props.board == true ?	path.concat(this.props.message.id) : '#message';
        return (
            <div className="message-header">
                <span><input type="checkbox" name="delete"/></span>
                <span className="message-title">{this.props.message.title}</span>
                <span>Anonymous</span>
                <span>{date}</span>
                <span>Id: {this.props.message.id}</span>
                <span>
				</span>
                <span>
				</span>
                <span>
				<a href={replyHref}>Reply</a>
				</span>
            </div>
        )
    }
}
// end::message[]

export default MessageHeader;
