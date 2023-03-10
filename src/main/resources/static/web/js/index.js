var app = new Vue({
    el:"#app",
    data:{
        password:"",
        company: "",
        maritalStatus: "",
        address: "",
        phone: "",
        eMail:"",
        firstName: "",
        lastName: "",
        errorToats:null,
        errorMsg: "",
        showSignUp: false,
    },
    methods:{
        signIn: function(event){
            event.preventDefault();
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/login',`eMail=${this.eMail}&password=${this.password}`,config)
                .then(response => window.location.href="/web/accounts.html")
                .catch(() =>{
                    this.errorMsg = "Sign in failed, check the information"
                    this.errorToats.show();
                })
        },
        signUp: function(event){
            event.preventDefault();
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&eMail=${this.eMail}&phone=${this.phone}&address=${this.address}&maritalStatus=${this.maritalStatus}&company=${this.company}&password=${this.password}`,config)
                .then(() => { this.signIn(event) })
                .catch(() =>{
                    this.errorMsg = "Sign up failed, check the information"
                    this.errorToats.show();
                })
        },
        showSignUpToogle: function(){
            this.showSignUp = !this.showSignUp;
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})