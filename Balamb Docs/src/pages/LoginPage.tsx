import { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthService from '../services/AuthService';
import styles from '../styleModules/LoginPage.module.css';
import { AuthContext } from '../auth/AuthContext.tsx';

const LoginComponent = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const { setIsLoggedIn } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await AuthService.login({ username, password });
            if (response.token !== 'Invalid credentials') {
                localStorage.setItem('token', response.token);
                setIsLoggedIn(true);
                navigate('/dashboard');
            } else {
                setMessage('Invalid credentials');
            }
        } catch (error) {
            setMessage('Invalid credentials');
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div className={styles['card-header']}>Login Form</div>
                <div className={styles['card-body']}>
                    {message && <div className={styles.alert}>{message}</div>}
                    <form onSubmit={handleLogin}>
                        <div className={styles['form-group']}>
                            <label>Username</label>
                            <input
                                type="text"
                                className={styles['form-control']}
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div className={styles['form-group']}>
                            <label>Password</label>
                            <input
                                type="password"
                                className={styles['form-control']}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        <button type="submit" className={styles['btn-primary']}>Login</button>
                    </form>
                    <div className={styles['mt-3']}>
                        <span>
                            Not registered?{" "}
                            <Link to="/register" className={styles.link}>
                                Register here
                            </Link>
                        </span>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default LoginComponent;
