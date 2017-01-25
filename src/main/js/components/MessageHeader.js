'use strict';

const React = require('react');
const Button = require('react-bootstrap/lib/Button')

class MessageHeader extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.message);
    }
    render() {
        let date = (new Date(this.props.message.timestamp)).toLocaleString();
        return (
            <div className="header">
                <span className="message-title">{this.props.message.title}</span>
                <span>Anonymous</span>
                <span>{date}</span>
                <span><a name={this.props.message.id} href={'#'+this.props.message.id}>{'#'+this.props.message.id}</a></span>
                {this.props.board == true ? null : <Button bsStyle="link" bsSize="xsmall" onClick={this.props.onDialogOpen}><img width="14px" height="14px" src="/img/round-right.png"/></Button>}
            </div>
        )
    }
}

export default MessageHeader;
