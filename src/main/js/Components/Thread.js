'use strict'

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::thread[]
class Thread extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.thread.board}</td>
				<td>{this.props.thread.title}</td>
				<td>{this.props.thread.text}</td>
				<td>{this.props.thread.timestamp}</td>
			</tr>
		)
	}
}
// end::thread[]

export default Thread;
