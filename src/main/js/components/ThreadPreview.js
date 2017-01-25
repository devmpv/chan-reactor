'use strict';

import Message from "./Message";

const React = require('react');
const client = require('../client');
const Button = require('react-bootstrap/lib/Button')
const Badge = require('react-bootstrap/lib/Badge')
const previewPath = '/rest/api/messages/search/preview';
const countPath = '/rest/api/messages/search/count';
const currentUrl = window.location.pathname.concat("/thread/");

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
            <div key={message.id}>
                <Message message={message}
                    onThumbClick={this.props.onThumbClick}
                    board={true}
                    onDelete={this.props.onDelete}
                    onDialogOpen={this.props.onDialogOpen}/>
            </div>
        );
        let count = this.state.count-this.state.messages.length;
        return (
            <div>
                <div className="post-wrapper">
                    <Message message={this.props.thread}
                        onThumbClick={this.props.onThumbClick}
                        board={true}
                        onDelete={this.props.onDelete}
                        onDialogOpen={this.props.onDialogOpen}/>
                        <Button href={currentUrl.concat(this.props.thread.id)} bsStyle="primary" bsSize="xsmall">Open <Badge title="Omitted replies">{count}</Badge></Button>
                </div>
                <div className="reply-panel">{replies}</div>
            </div>
        )
    }
}

export default ThreadPreview;
