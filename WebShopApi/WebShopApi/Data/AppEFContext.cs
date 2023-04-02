using Microsoft.EntityFrameworkCore;
using WebShopApi.Data.Entities;

namespace WebShopApi.Data
{
    public class AppEFContext : DbContext
    {
        public AppEFContext(DbContextOptions<AppEFContext> options)
            : base(options) { }

        public DbSet<CategoryEntity> Categories { get; set; }
    }
}
