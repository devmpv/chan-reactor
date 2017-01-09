'use strict';

const React = require('react');

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
                {this.props.board == true ? null : <span><a className="postbtn" href="#message"><img width="14px" height="14px" src="/img/round-right.png"/></a></span>}
            </div>
        )
    }
}

export default MessageHeader;
