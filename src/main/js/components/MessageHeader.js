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
            <div className="header">
                <span className="message-title">{this.props.message.title}</span>
                <span>Anonymous</span>
                <span>{date}</span>
                <span><a name={this.props.message.id} href={'#'+this.props.message.id}>{'#'+this.props.message.id}</a></span>
                <span>
				</span>
                <span>
				</span>
                <span>
				{this.props.board == true ? <a href={replyHref}>Open</a> : <a href={replyHref}>Reply</a>}
				</span>
            </div>
        )
    }
}
// end::message[]

export default MessageHeader;
