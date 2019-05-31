ALTER PROCEDURE dbo.SP_FINAL_PRICE 
    @orderId int = 0,
    @finalPrice DECIMAL(10,3) OUTPUT 
AS
	SET NOCOUNT ON;

    declare @kursor cursor
	declare @idAr int
	declare @cnt int
	declare @discount int
	declare @price int

	select @finalPrice = 0

	set @kursor = cursor for
	select IdArticle,Count
	from OrderItems
	where IdOrder = @orderId

	open @kursor

	fetch next from @kursor
	into @idAr,@cnt

	while @@FETCH_STATUS = 0
	begin
		select @discount = s.DiscountPercentage, @price = a.ArticlePrice
		from Article a join Shop s on (a.IdShop=s.Id)
		where a.Id = @idAr

		select @finalPrice = @finalPrice + (@cnt * @price)*(@discount/100.0)

		fetch next from @kursor
		into @idAr,@cnt
	end


RETURN 0 