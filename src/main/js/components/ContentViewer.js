'use strict';

// tag::vars[]
const React = require('react');
const block = {
        display: 'block'
      };
const none = {
        display: 'none'
      };
// end::vars[]

// tag::message[]
class ContentViewer extends React.Component {

    constructor(props) {
        super(props);
    }
    
    render() {
        let style = this.props.content.visible ? block : none;
        return (
            <div style={style} id="content-viewer">
                <img id='content' src={this.props.content.src} onClick={this.props.onThumbClick}/>
            </div>
        )
    }
}
// end::message[]

export default ContentViewer;
