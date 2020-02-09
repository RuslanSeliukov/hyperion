const React = require('react');
const ReactDOM = require('react-dom');
import { BrowserRouter, Route } from 'react-router-dom';
import { Link } from 'react-router-dom';
import MakeWork from './makeWork';
import EmployeeReport from './employeeReport';
import ClassWorkReport from './classWorkReport';

class App extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <MainPage />
        )
    }
}

class MainPage extends React.Component {
    render() {
        return (

            <div className="container">
                <div className="row align-self-center mt-3" >
                    <div className="col-sm text-center">
                        <h3 className="logo">HYPERION</h3>
                    </div>
                </div>
                <div className="row mt-5">
                    <div className="col-sm">
                        <nav>
                            <Link to="/makeWork">Создать Задание</Link>
                        </nav>
                    </div>
                    <div className="col-sm">
                        <nav>
                            <Link to="/employeeReport">Отчет По Сотруднику</Link>
                        </nav>
                    </div>
                    <div className="col-sm">
                        <nav>
                            <Link to="/classWorkReport">Отчет По Виду Дополнительной Работы</Link>
                        </nav>
                    </div>
                </div>
                <Route exact path="/makeWork" component={MakeWork}/>
                <Route exact path="/employeeReport" component={EmployeeReport}/>
                <Route exact path="/classWorkReport" component={ClassWorkReport}/>
            </div>
        )
    }
}

ReactDOM.render((
    <BrowserRouter>
        <App />
    </BrowserRouter>
), document.getElementById('react'));
