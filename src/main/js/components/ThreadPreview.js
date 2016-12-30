'use strict';

import Message from "./Message";

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::message[]
class ThreadPreview extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="panel">
                <Message message={this.props.thread}
                onThumbClick={this.props.onThumbClick}
                threadView={false}
                onDelete={this.props.onDelete}/>
            </div>
        )
    }
}
// end::message[]

export default ThreadPreview;
