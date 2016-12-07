'use strict';

// tag::vars[]
const React = require('react');
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
		return (
			<div className="message-header">
				<span><input type="checkbox" name="delete"/></span>
				<span className="message-title">{this.props.thread.title}</span>
				<span>Anonymous</span>
				<span>{date}</span>
				<span>
					<button onClick={this.handleDelete}>Delete</button>
				</span>
				<span>
					<button>Hide</button>
				</span>
			</div>
		)
	}
}
// end::thread[]

export default MessageHeader;
