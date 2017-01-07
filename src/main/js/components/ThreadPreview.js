'use strict';

import Message from "./Message";

const React = require('react');
const client = require('../client');
const previewPath = '/rest/api/messages/search/preview';
const countPath = '/rest/api/messages/search/count';

class ThreadPreview extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            count: 0,
            messages: []
        }
    }

    componentDidMount() {
        client({
            method: 'GET', path: previewPath, params: {
                id: this.props.thread.id
            }
        }).done(preview => {
            this.setState({
                messages: preview.entity._embedded.messages.reverse()
            });

        });
        client({
            method: 'GET', path: countPath, params: {
                id: this.props.thread.id
            }
        }).done(count => {
            this.setState({
                count: count.entity,
            });
        });

    }

    render() {
        let replies = this.state.messages.map(message=>
            <Message key={message.id} message={message}
            onThumbClick={this.props.onThumbClick}
            board={false}
            onDelete={this.props.onDelete}/>
        );
        return (
            <div>
                <Message message={this.props.thread}
                onThumbClick={this.props.onThumbClick}
                board={true}
                onDelete={this.props.onDelete}/>
                <div>Replies: {this.state.count}</div>
                <div className="panel">
                  {replies}
                </div>
            </div>
        )
    }
}
// end::message[]

export default ThreadPreview;
