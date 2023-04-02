using System.ComponentModel.DataAnnotations;

namespace WebShopApi.Data.Entities
{
    public interface IEntity<T>
    {
        T Id { get; set; }
        bool isDelete { get; set; }
        DateTime DateCreated { get; set; }
    }
    public class BaseEntity<T> : IEntity<T>
    {
        [Key]
        public T Id { get; set; }
        public bool isDelete { get; set; }
        public DateTime DateCreated { get; set; }
    }
}
