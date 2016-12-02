'use strict';

import ThreadList from './components/ThreadList';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {threads: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/api/threads'}).then(response => {
			this.setState({threads: response.entity._embedded.threads});
		});
	}

	render() {
		return (
			<ThreadList threads={this.state.threads}/>
		)
	}
}
// end::app[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
);
// end::render[]
