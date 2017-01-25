'use strict';

const React = require('react');
const client = require('../client');
const boardsPath = '/rest/api/boards';
const ListGroup = require('react-bootstrap/lib/ListGroup')
const ListGroupItem = require('react-bootstrap/lib/ListGroupItem')

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
            <ListGroupItem header={'/' + board.id} key={board._links.self.href} href={board.id}>
              {board.title}
            </ListGroupItem>
        );
        return (
            <div className="panel">
                <div>
                    <h4>Board List</h4>
                </div>
                <ListGroup>
                    {boards}
                </ListGroup>

            </div>
        )
    }
}

export default MainPage;
