const app = new Vue({
    el: '#app',
    data: {
        eMail: "",
        firstName: "",
        lastName: "",
        outPut: "",
        clients: []
    },
    methods:{
        // load and display JSON sent by server for /clients
        loadData: function() {
            axios.get("/clients")
                .then(function (response) {
                    // handle success
                    app.outPut = response.data;
                    app.clients = response.data._embedded.clients;
                })
                .catch(function (error) {
                    // handle error
                    app.outPut = error;
                })
        },
        // handler for when user clicks add client
        addClient: function() {
            if (app.eMail.length > 1 && app.firstName.length > 1 && app.lastName.length > 1) {
                this.postPlayer(app.eMail);
            }
        },
        // code to post a new player using AJAX
        // on success, reload and display the updated data from the server
        postPlayer: function(email) {
            axios.post("clients",{ "eMail":email, "firstName": app.firstName, "lastName": app.lastName })
                .then(function (response) {
                    // handle success
                    showOutput = "Saved -- reloading";
                    app.loadData();
                })
                .catch(function (error) {
                    // handle error
                    app.outPut = error;
                })
        }
    },
    mounted(){
        this.loadData();
    }
});
