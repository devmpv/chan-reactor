'use strict';

// tag::vars[]
const React = require('react');
// end::vars[]

// tag::message[]
class ContentViewer extends React.Component {

    constructor(props) {
        super(props);
        this.close=this.close.bind(this);
        this.handleScroll=this.handleScroll.bind(this);
    }

    handleScroll(event) {
        let zoom = document.getElementById('content').style.zoom;
    }
    
    close() {
        document.getElementById('content-viewer').style.display='none';
    }
    
    render() {
        return (
            <div id="content-viewer" onScroll={this.handleScroll}>
                <img id='content' src={this.props.contentSrc} onClick={this.close}/>
            </div>
        )
    }
}
// end::message[]

export default ContentViewer;
