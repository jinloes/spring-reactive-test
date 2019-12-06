import React, {Component} from 'react';
import './App.css';
import {Container} from 'reactstrap';

class Home extends Component {
    render() {
        return (
            <div>
                <Container fluid>
                    Welcome Home!
                </Container>
            </div>
        );
    }
}

export default Home;