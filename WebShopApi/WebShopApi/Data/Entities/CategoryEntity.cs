using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using WebShopApi.Data.Entities.Identity;

namespace WebShopApi.Data.Entities
{
    [Table("tblCategories")]
    public class CategoryEntity : BaseEntity<int>
    {
        [Required, StringLength(255)]
        public string Name { get; set; }
        [StringLength(255)]
        public string Image { get; set; }
        public int Priority { get; set; }
        [StringLength(4000)]
        public string Description { get; set; }
        [ForeignKey("User")]
        public int? UserId { get; set; }
        public virtual UserEntity User { get; set; }
    }
}
