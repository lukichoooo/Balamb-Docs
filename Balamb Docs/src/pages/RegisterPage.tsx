import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthService from '../services/AuthService';
import styles from '../styleModules/AuthPage.module.css';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await AuthService.register({ username, password });
            if (response) {
                setMessage('Registration successful! You can now log in.');
                navigate('/login');
            } else {
                setMessage('Registration failed');
            }
        } catch (error) {
            setMessage('Registration failed');
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div className={styles['card-header']}>Register</div>
                <div className={styles['card-body']}>
                    {message && <div className={styles.alert}>{message}</div>}
                    <form onSubmit={handleRegister}>
                        <div className={styles['form-group']}>
                            <p className={styles['form-title']}>Create New Account</p>
                            <label>Username</label>
                            <input
                                type="text"
                                className={styles['form-control']}
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div className={styles['form-group']}>
                            <label>Password</label>
                            <input
                                type="password"
                                className={styles['form-control']}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className={styles['btn-primary']}>Register</button>
                    </form>
                    <div className={styles['mt-3']}>
                        <span>
                            Already have an account?{" "}
                            <Link to="/login" className={styles.link}>
                                Login here
                            </Link>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
