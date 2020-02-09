const React = require('react');
import CustomSelect from './customSelect';
import CustomSelect2 from './customSelect2';
import $ from 'jquery';

class MakeWork extends React.Component {

    constructor(props) {
        super(props);
        this.state = { showComponent: 0, workName: "", description: "", amountOfTime: "", workClass: "", dateStart: "", dateEnd: "", selectNumber: 0};
        this.setCountOfElements = this.setCountOfElements.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleChange2 = this.handleChange2.bind(this);
        window.selects = [[], [], [], [], [], [], [], [], [], []];
        window.employeeChosen = "";
    }

    setCountOfElements(count) {
        let value1 = this.state.showComponent;
        let value2 = value1 + count;
        this.setState({ showComponent: value2 });
    }

    renderEmployeeSelect() {
        let children = [];
        for(let i = 0; i < this.state.showComponent; i++) {
            this.state.selectItem = i;
            children.push(<CustomSelect2 link = "http://localhost:8080/api/getEmployees"
                                         setCountOfElements = {this.setCountOfElements}
                                         number = {this.state.selectItem} />)
        }
        return children;
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleChange2(event) {
        this.setState({workClass: event.label});
    }

    handleChange3(event) {
        window.employeeChosen = event.label;
    }

    showError() {
        $( "div.ui-state-error" ).html("<p class=mt-3>Ошибка валидации. Форма не была отправленна.</p>").css("color", "red");
    }

    handleSubmit(event) {
        $( "div.ui-state-error" ).html("");
        if (this.validate()) {
            this.showError();
        } else {

            window.selects.push(window.employeeChosen);

            let data = {
                workClass: this.state.workClass,
                workName: this.state.workName,
                description: this.state.description,
                dateStart: this.state.dateStart,
                dateEnd: this.state.dateEnd,
                employees: window.selects,
                amountOfTime: this.state.amountOfTime
            };

            window.location.reload();

            fetch('http://localhost:8080/api/submitForm', {
                method: 'post',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then(res => res.json())
                .then(res => window.location.reload());
        }
        event.preventDefault();
    }


    validate() {
        var isErrorsPresent = false;

        var employees = window.selects.slice();
        employees.push(window.employeeChosen);

        employees.length >= 11 ? isErrorsPresent = true : isErrorsPresent = false;

        this.checkIfDuplicateExists(employees) ? isErrorsPresent = true : isErrorsPresent = false;

        if ((this.state.dateStart === "" || this.state.dateEnd === "") ||
            (this.state.dateStart === "" && this.state.dateEnd === "")) {
            isErrorsPresent = true;
        }

        if ((this.isDateBeforeToday(this.state.dateStart) || this.isDateBeforeToday(this.state.dateEnd)) ||
            (this.isDateBeforeToday(this.state.dateStart) && this.isDateBeforeToday(this.state.dateEnd))) {
            isErrorsPresent = true;
        }

        if (new Date(this.state.dateStart).getTime() >= new Date(this.state.dateEnd).getTime()) {
            isErrorsPresent = true
        }

        if (!this.state.amountOfTime.match(/^[0-9]+$/)) {
            isErrorsPresent = true;
        }

        const timeDiff = (new Date(this.state.dateEnd)) - (new Date(this.state.dateStart));
        const hours = (timeDiff / (1000 * 60 * 60 * 24)) * 24;

        if (this.state.amountOfTime > hours) {
            isErrorsPresent = true;
        }

        return isErrorsPresent;
    }

    isDateBeforeToday(date) {
        return new Date(date) < new Date();
    }



    checkIfDuplicateExists(w){
        return new Set(w).size !== w.length
    }




    render() {
        return (
            <div>
                <div className="ui-state-error"></div>
                <form onSubmit={this.handleSubmit}>
                    <div className="row mt-3">
                        <div className="col-sm form-group">
                            <div className="mt-3">
                                <label className="ml-2" htmlFor="workClass">Выберете Класс Доп. Роботы: </label>
                                <CustomSelect className="form-control" name="workClass" link="http://localhost:8080/api/getWorkClass"
                                              handleChange2={this.handleChange2}/>
                            </div>
                            <div className="mt-3">
                                <label className="ml-2" htmlFor="workName">Введите Название Доп. Роботы: </label>
                                <input name="workName" className="form-control" type="text" size="40" onChange={this.handleChange}/>
                            </div>
                            <div className="mt-3">
                                <label className="ml-2" htmlFor="description">Введите Описание Доп. Роботы: </label>
                                <textarea name="description" className="form-control" cols="40" rows="3" onChange={this.handleChange}/>
                            </div>
                            <div className="mt-3">
                                <label className="ml-2" htmlFor="dateStart">Дата Начала Выполнения Доп. Роботы: </label>
                                <input type="date" name="dateStart" className="form-control"
                                       min="2019-01-01" max="2021-01-01" onChange={this.handleChange}/>
                            </div>
                            <div className="mt-3">
                                <label className="ml-2" htmlFor="dateEnd">Дата Окончания Выполнения Доп. Роботы: </label>
                                <input type="date" name="dateEnd" className="form-control"
                                       min="2019-01-01" max="2021-01-01" onChange={this.handleChange}/>
                            </div>
                        </div>

                        <div className="col-sm form-group">
                            <div className="ml-2 mt-3 row">
                                <label htmlFor="employeeChosen">Выбрать Сотрудника: </label>
                            </div>

                            <div className="col">
                                <div className="row">
                                    <div className="col-8 pl-0">
                                        <CustomSelect className="form-control col-8" name="employeeChosen"
                                                      link="http://localhost:8080/api/getEmployees"
                                                      handleChange2={this.handleChange3}/>
                                    </div>
                                    <div className="col-4">
                                        <input type="button" value="Добавить" className="btn btn-primary btn-block"
                                               onClick={() => this.setCountOfElements(1)}/>
                                    </div>
                                </div>
                                {
                                    this.renderEmployeeSelect()
                                }
                            </div>

                            <div className="mt-3">
                                <label className="ml-2" htmlFor="amountOfTime">Введите Количество Времени: </label>
                                <input className="form-control" name="amountOfTime" type="text" size="40" onChange={this.handleChange}/>
                            </div>
                        </div>

                    </div>

                    <input type="submit" value="Отправить" className="btn btn-primary"/>

                </form>
            </div>
        )
    }
} export default MakeWork;