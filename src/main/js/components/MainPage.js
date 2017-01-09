'use strict';

const React = require('react');
const client = require('../client');
const boardsPath = '/rest/api/boards';

class MainPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {boards: [], attributes: []};
    }

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

    componentDidMount() {
        this.loadFromServer();
    }

    render() {
        let boards = this.state.boards.map(board =>
            <div key={board._links.self.href}>
                <span><a href={board.id}>/{board.id}</a></span>
                <span>{board.title}</span>
            </div>
        );
        return (
            <div className="panel">
                <div>
                    <h3>Board List</h3>
                </div>
                {boards}
            </div>
        )
    }
}

export default MainPage;
