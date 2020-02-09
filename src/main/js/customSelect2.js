const React = require('react');
import Select from 'react-select'

class CustomSelect2 extends React.Component {

    constructor(props) {
        super(props);
        this.state = {options: []};
    }

    componentDidMount() {
        fetch(this.props.link)
            .then(res => res.json())
            .then((data) => {
                this.setState({options: data})
            })
            .catch(console.log);
    }

    handleChange(event) {
        let selectNumber = this.name.charAt(this.name.indexOf('_') + 1);
        window.selects[selectNumber] = event.label;
    }

    render() {
        const name = "select_" + this.props.number;

        return (
            <div className="mt-3 row">
                <div className="col-8 pl-0">
                    <Select name = {name} options={this.state.options} onChange = {this.handleChange} />
                </div>
                <div className="col-4">
                    <input className="btn btn-danger btn-block" type="button" value="Убрать" onClick={() => this.props.setCountOfElements(-1)}/>
                </div>
            </div>
        )

    }
}

export default CustomSelect2;