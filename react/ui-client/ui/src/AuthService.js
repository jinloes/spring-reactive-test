class AuthService {

    static isAuthenticated() {
        return localStorage.getItem("authToken") != null;
    }

}

export default AuthService;