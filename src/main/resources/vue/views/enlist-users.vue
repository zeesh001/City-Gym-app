<template id="enlist-users">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
      <table>
        <tr>
          <th>&nbsp &nbsp &nbsp</th>
          <th>Id </th>
          <th> &nbsp&nbsp &nbsp &nbsp &nbsp </th>

          <th>Name </th>
          <th> &nbsp &nbsp   &nbsp &nbsp &nbsp</th>

          <th>Email  </th>
          <th> &nbsp &nbsp &nbsp  &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>

          <th>Phone  </th>
          <th> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Gender  </th>
          <th> &nbsp  &nbsp &nbsp</th>
          <th>Adddress  </th>
          <th> &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp</th>
          <th>Trainer  </th>
          <th> &nbsp &nbsp   &nbsp &nbsp &nbsp &nbsp</th>
          <th>Training slot</th>
          <th> &nbsp &nbsp  &nbsp&nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Service  </th>



        </tr>
      </table>

      <div class="list-group-item d-flex align-items-start"
           v-for="(service,index) in services" v-bind:key="index">

        <div class="mr-auto p-2">
          <table style="width: 50px">
            <tr > {{ service.id }}</tr>
            <tr>&nbsp&nbsp &nbsp &nbsp &nbsp  </tr>

            <tr > {{ service.name }}</tr>
            <tr> &nbsp &nbsp &nbsp &nbsp</tr>

            <tr > {{ service.email }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp</tr>
            <tr> {{ service.phone }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.gender }}</tr>
            <tr>  &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.address }}</tr>
            <tr>  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.trainer }}</tr>
            <tr>  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.timing_slot }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.service_name }}</tr>


          </table>
        </div>

        <div class="p2">
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteUser(service, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>

      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("enlist-users", {
  template: "#enlist-users",
  data: () => ({
    services: [],
    formData: [],
    noUserFound: false,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      const name = this.$javalin.pathParams["service_name"];
      axios.get(`/api/users/service/${name}`)
          .then(res => this.services = res.data)
          .catch(error => {
            console.log("No user found for id passed in the path parameter: " + error)
            this.noUserFound = true
          });
    },
    deleteUser: function (service, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const serv = service.id;
        const url = `/api/users/${serv}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.services.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

  }
});
</script>