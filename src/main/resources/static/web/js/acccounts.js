var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        accountCorriente: [],
        accountAhorro: [],
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.accountCorriente = this.clientInfo.accounts.filter(account => account.accountType == "CORRIENTE");
                    this.accountAhorro = this.clientInfo.accounts.filter(account => account.accountType == "AHORRO");
                })
                .catch((error)=>{
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        createCorriente: function(){
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post(`/api/clients/current/accounts?accountType=${"CORRIENTE"}`,config)
                .then(response => window.location.reload())
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        createAhorro: function(){
            let config = {
                headers: {
                     'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post(`/api/clients/current/accounts?accountType=${"AHORRO"}`,config)
                .then(response => window.location.reload())
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})