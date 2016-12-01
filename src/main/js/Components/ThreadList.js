'use strict'

import Thread from './Thread';

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::ThreadList[]
class ThreadList extends React.Component{
	render() {
		var threads = this.props.threads.map(thread =>
			<Thread key={thread._links.self.href} thread={thread}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Board</th>
						<th>Title</th>
						<th>Text</th>
						<th>Timestamp</th>
					</tr>
					{threads}
				</tbody>
			</table>
		)
	}
}
// end:ThreadList[]

export default ThreadList;
