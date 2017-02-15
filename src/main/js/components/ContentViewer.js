'use strict';

const React = require('react');
const block = {
    display: 'block'
};
const none = {
    display: 'none'
};

class ContentViewer extends React.Component {
    handleThumbClick : (event : SyntheticInputEvent) => void;
    constructor(props) {
        super(props);
        this.handleThumbClick = (event : SyntheticInputEvent) => {
            this
                .props
                .onThumbClick('');
        }
    }

    render() {
        let style = this.props.content.visible
            ? block
            : none;
        return (
            <div style={style} id="content-viewer">
                <img id="content" src={this.props.content.src} onClick={this.handleThumbClick}/>
            </div>
        )
    }
}

ContentViewer.propTypes = {
    content: React.PropTypes.object.isRequired,
    onThumbClick: React.PropTypes.func.isRequired
}

export default ContentViewer;
