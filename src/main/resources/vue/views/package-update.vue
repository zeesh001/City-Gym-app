<template id="package-update">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateService()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-service_name">Service Name</span>
            </div>
            <input type="text" class="form-control" v-model="service.service_name" name="service_name" placeholder="Service Name"/>
          </div>


        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("package-update", {
  template: "#package-update",
  data: () => ({
    service: null,
    noUserFound: false,
  }),
  created: function () {
    const userId = this.$javalin.pathParams["id"];
    const url = `/api/packages/${userId}`
    axios.get(url)
        .then(res => this.service = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        });
  },
  methods: {
    updateService: function () {
      const userId = this.$javalin.pathParams["id"];
      const url = `/api/packages/${userId}`
      axios.patch(url,
          {

            service_name: this.service.service_name,
          })
          .then(response =>
              this.service.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
  }
});


</script>