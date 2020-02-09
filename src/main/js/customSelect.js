const React = require('react');
import Select from 'react-select'

class CustomSelect extends React.Component {

    constructor(props) {
        super(props);
        this.state = { options: [] };
    }

    componentDidMount() {
        fetch(this.props.link)
            .then(res => res.json())
            .then((data) => {
                this.setState({options: data})
            })
            .catch(console.log);
    }

    render() {
        return (
            <Select options={this.state.options} onChange = {this.props.handleChange2}/>
        )
    }
}

export default CustomSelect;