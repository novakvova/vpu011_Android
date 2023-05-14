using WebShopApi.Data.Entities.Identity;

namespace WebShopApi.Abastract
{
    public interface IJwtTokenService
    {
        Task<string> CreateToken(UserEntity user);
    }
}
