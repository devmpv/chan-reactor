'use strict';

// tag::vars[]
const React = require('react');
const client = require('../client');
const boardsPath = '/api/boards';
// end::vars[]

class MainPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {boards: [], attributes: []};
    }

    // tag::follow-2[]
    loadFromServer() {
        client({method: 'GET', path: boardsPath})
            .done(boardCollection => {
                let boards = boardCollection.entity._embedded.boards;
                this.setState({
                    boards: boards,
                    attributes: ['id', 'title'],
                });
            });
    }

    // end::follow-2[]

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer();
    }

    // end::follow-1[]

    // tag::message-list-render[]
    render() {
        let boards = this.state.boards.map(board =>
            <div className="panel" key={board._links.self.href}>
                <span><a href={board.id}>/{board.id}</a></span>
                <span>{board.title}</span>
            </div>
        );
        return (
            <div>
                <div>
                    <h3>Board List</h3>
                </div>
                {boards}
            </div>
        )
    }

    // end::message-list-render[]
}

export default MainPage;
