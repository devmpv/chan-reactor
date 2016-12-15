'use strict';

import Message from './Message';

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::thread[]
class ThreadPreview extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let thread = this.props.thread;
        return (
            <div className="panel">
                <Message thread={thread} onDelete={this.props.onDelete}/>
            </div>
        )
    }
}
// end::thread[]

export default ThreadPreview;
