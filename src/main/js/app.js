'use strict';

import CreateDialog from './components/CreateDialog';
import ThreadList from './components/ThreadList';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {threads: [], attributes: [], pageSize: 20, links: {}};
		this.updatePageSize = this.updatePageSize.bind(this);
		this.onCreate = this.onCreate.bind(this);
		this.onDelete = this.onDelete.bind(this);
		this.onNavigate = this.onNavigate.bind(this);
	}

	// tag::follow-2[]
	loadFromServer(pageSize) {
		follow(client, root, [
			{rel: 'threads', params: {size: pageSize}}]
		).then(threadCollection => {
			return client({
				method: 'GET',
				path: threadCollection.entity._links.profile.href,
				headers: {'Accept': 'application/schema+json'}
			}).then(schema => {
				this.schema = schema.entity;
				return threadCollection;
			});
		}).done(threadCollection => {
			this.setState({
				threads: threadCollection.entity._embedded.threads,
				attributes: ['title', 'text', 'board'],//Object.keys(this.schema.properties),
				pageSize: pageSize,
				links: threadCollection.entity._links});
		});
	}
	// end::follow-2[]

	// tag::create[]
	onCreate(newThread) {
		follow(client, root, ['threads']).then(threadCollection => {
			return client({
				method: 'POST',
				path: threadCollection.entity._links.self.href,
				entity: newThread,
				headers: {'Content-Type': 'application/json'}
			})
		}).then(response => {
			return follow(client, root, [
				{rel: 'threads', params: {'size': this.state.pageSize}}]);
		}).done(response => {
			this.onNavigate(response.entity._links.last.href);
		});
	}
	// end::create[]

	// tag::delete[]
	onDelete(thread) {
		client({method: 'DELETE', path: thread._links.self.href}).done(response => {
			this.loadFromServer(this.state.pageSize);
		});
	}
	// end::delete[]

	// tag::navigate[]
	onNavigate(navUri) {
		client({method: 'GET', path: navUri}).done(threadCollection => {
			this.setState({
				threads: threadCollection.entity._embedded.threads,
				attributes: this.state.attributes,
				pageSize: this.state.pageSize,
				links: threadCollection.entity._links
			});
		});
	}
	// end::navigate[]

	// tag::update-page-size[]
	updatePageSize(pageSize) {
		if (pageSize !== this.state.pageSize) {
			this.loadFromServer(pageSize);
		}
	}
	// end::update-page-size[]

	// tag::follow-1[]
	componentDidMount() {
		this.loadFromServer(this.state.pageSize);
	}
	// end::follow-1[]

	render() {
		return (
			<div>
				<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
				<ThreadList threads={this.state.threads}
							  links={this.state.links}
							  pageSize={this.state.pageSize}
							  onNavigate={this.onNavigate}
							  onDelete={this.onDelete}
							  updatePageSize={this.updatePageSize}/>
			</div>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
);
