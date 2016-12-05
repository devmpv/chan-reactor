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
		return (
			<th>
				<td><a href={this.props.thread._links.self.href}>link</a></td>
				<td>{this.props.thread.board}</td>
				<td>{this.props.thread.title}</td>
				<td>{this.props.thread.timestamp}</td>
				<td>
					<button onClick={this.handleDelete}>Hide</button>
				</td>
			</th>
		)
	}
}
// end::thread[]

export default MessageHeader;
