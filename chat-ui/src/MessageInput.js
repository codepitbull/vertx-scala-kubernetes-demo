import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import TextField from 'material-ui/TextField';
import {blue500, blue600} from 'material-ui/styles/colors';
import './MessageInput.css';


const styles = {
    floatingLabelStyle: {
        color: blue600,
    },
    floatingLabelFocusStyle: {
        color: blue500,
    },
};

class MessageInput extends Component {

    constructor(props) {
        super(props);
        this.state = {msg: ""};
    }

    handleInputChange(event) {
        this.setState({
            msg: event.target.value
        })
    }

    cleanInput() {
        this.setState({
            msg: ''
        })
    }

    render() {
        return (
            <div className="bottomFeeder">
                <MuiThemeProvider>
                    <TextField
                        floatingLabelText="Type your message to the world"
                        floatingLabelStyle={styles.floatingLabelStyle}
                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                        value={this.state.msg}
                        onChange={this.handleInputChange.bind(this)}
                        onKeyPress={(ev) => {
                            if (ev.key === 'Enter') {
                                // Do code here
                                ev.preventDefault();
                                this.props.sendMsg(this.state.msg);
                                this.cleanInput()
                            }
                        }}
                    />
                </MuiThemeProvider>
            </div>
        );
    }
}

export default MessageInput;