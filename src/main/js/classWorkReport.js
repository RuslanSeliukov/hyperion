const React = require('react');
import CustomSelect from './customSelect';
import CustomSelect2 from './customSelect2';
import $ from 'jquery';

class ClassWorkReport extends React.Component {


    constructor(props) {
        super(props);
        this.state = { dateStart: "", dateEnd: "", classWork: ""};
        this.handleChange = this.handleChange.bind(this);
        this.handleChange2 = this.handleChange2.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        window.employeeChosen = "";
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleChange2(event) {
        this.setState({classWork: event.label});
    }

    validate() {
        var isErrorsPresent = false;

        if ((this.state.dateStart === "" || this.state.dateEnd === "") ||
            (this.state.dateStart === "" && this.state.dateEnd === "")) {
            isErrorsPresent = true;
        }

        if (new Date(this.state.dateStart).getTime() >= new Date(this.state.dateEnd).getTime()) {
            isErrorsPresent = true
        }

        return isErrorsPresent;
    }

    isDateBeforeToday(date) {
        return new Date(date) < new Date();
    }

    handleSubmit(event) {
        $( "div.ui-state-error" ).html("");
        if (this.validate()) {
            this.showError();
        } else {

            let data = {
                workClass: this.state.classWork,
                dateStart: this.state.dateStart,
                dateEnd: this.state.dateEnd
            };

            fetch('http://localhost:8080/api/submitClassWorkReport', {
                method: 'post',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then((response) => {
                const filename =  response.headers.get('Content-Disposition').split('filename=')[1];
                response.blob().then(blob => {
                    let url=  window.URL.createObjectURL(blob);
                    let a = document.createElement('a');
                    a.href = url;
                    a.download = filename;
                    a.click();
                });
            });
        }
        event.preventDefault();
    }

    showError() {
        $( "div.ui-state-error" ).html("<p class=mt-3>Ошибка валидации. Форма не была отправленна.</p>").css("color", "red");
    }

    render() {
        return (
            <div>
                <div className="ui-state-error"></div>
                <form onSubmit={this.handleSubmit}>
                    <div className="mt-3">
                        <label className="ml-2" htmlFor="start">Выберите Класс Работ: </label>
                        <CustomSelect className="form-control" name="employee" link="http://localhost:8080/api/getWorkClass" handleChange2={this.handleChange2} />
                    </div>
                    <div className="mt-3">
                        <label className="ml-2" htmlFor="start">Дата Начала Выполнения Доп. Роботы: </label>
                        <input className="form-control" type="date" name="dateStart" min="2019-01-01" max="2021-01-01" onChange={this.handleChange}/>
                    </div>
                    <div className="mt-3">
                        <label className="ml-2" htmlFor="end">Дата Окончания Выполнения Доп. Роботы: </label>
                        <input className="form-control" type="date" name="dateEnd" min="2019-01-01" max="2021-01-01" onChange={this.handleChange}/>
                    </div>
                    <input className="btn btn-primary mt-3" type="submit" value="Отправить" />
                </form>
            </div>
        )
    }

} export default ClassWorkReport;